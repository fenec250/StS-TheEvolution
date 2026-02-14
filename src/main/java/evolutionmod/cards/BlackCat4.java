package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
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
import evolutionmod.patches.EvolutionEnum;

public class BlackCat4
        extends BaseEvoCard implements StartupCard {
    public static final String ID = "evolutionmodV2:BlackCat";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BlackCat.png";
    private static final int COST = -1;
    private static final int DAMAGE_AMT = 3;
    private static final int UPGRADE_DAMAGE_AMT = 1;

    public static int refundPool = 0;
    public boolean firstTurnFlag = false;

    public BlackCat4() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
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
//        if (x > 0) {
//            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, x,false)));
//        }

//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                ShadowsPower.triggerImmediately(p);
//                this.isDone = true;
//            }
//        });
        AbstractCard anchor = this;
        if (!freeToPlayOnce) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    p.energy.use(energyOnUse);
                    int refund = Math.min(energyOnUse, refundPool);
                    addToTop(new RefundAction(anchor, refund));
                    refundPool -= refund;
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void applyPowers() {
        alterDamageAround(super::applyPowers);
        rawDescription = EXTENDED_DESCRIPTION[0] + (refundPool > 0 ? EXTENDED_DESCRIPTION[1] + refundPool + EXTENDED_DESCRIPTION[2] : EXTENDED_DESCRIPTION[3]);
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
    public boolean atBattleStartPreDraw() {
        firstTurnFlag = true;
        return false;
    }

    @Override
    public void atTurnStart() {
        refundPool = GameActionManager.turn + (firstTurnFlag ? 0 : 1); // not updated to current turn yet
        firstTurnFlag = false;
        super.atTurnStart();
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackCat4();
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
