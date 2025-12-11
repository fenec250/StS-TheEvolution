package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.ShadowsPower;

public class BlackCat3
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:BlackCat";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BlackCat.png";
    private static final int COST = -1;
    private static final int DAMAGE_AMT = 3;
    private static final int UPGRADE_DAMAGE_AMT = 1;

    public BlackCat3() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = DAMAGE_AMT;
//        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = energyOnUse;
        if (p.hasRelic(ChemicalX.ID)) {
            x += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        addToBot(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (x > 0) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, x,false)));
        }

//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                ShadowsPower.triggerImmediately(p);
//                this.isDone = true;
//            }
//        });
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!freeToPlayOnce) {
                    p.energy.use(energyOnUse);
                }
                this.isDone = true;
            }
        });
        AbstractCard anchor = this;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int refund = ((int) AbstractDungeon.getCurrRoom().monsters.monsters.stream()
                        .filter(m -> m.hasPower(WeakPower.POWER_ID))
                        .count());
                if (refund > 0)
                    addToTop(new RefundAction(anchor, refund));
                this.isDone = true;
            }
        });
    }

    @Override
    public void applyPowers() {
        alterDamageAround(super::applyPowers);
        rawDescription = EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        alterDamageAround(() -> super.calculateCardDamage(mo));
    }

    private void alterDamageAround(Runnable supercall) {
        int x = EnergyPanel.getCurrentEnergy();
        if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) {
            x += 2;
        }
        this.baseDamage = this.magicNumber * x;
        supercall.run();
        this.baseDamage = this.magicNumber * x;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackCat3();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_DAMAGE_AMT);
            //this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
//
//    @Override
//    public void triggerOnGlowCheck() {
//        if ((EnergyPanel.getCurrentEnergy() % 2) == (upgraded ? 1 : 0)) {
//            this.glowColor = GOLD_BORDER_GLOW_COLOR;
//        } else {
//            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
//        }
//    }

//    @Override
//    public List<TooltipInfo> getCustomTooltips() {
//        if (customTooltips == null) {
//            super.getCustomTooltips();
//            customTooltips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0],
//                    EXTENDED_DESCRIPTION[1]));
//        }
//        return  customTooltips;
//    }
}
