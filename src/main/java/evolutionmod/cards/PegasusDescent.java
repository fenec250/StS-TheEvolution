package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.patches.AbstractCardEnum;

public class PegasusDescent
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:PegasusDescent";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Pegasus.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 7;
    private static final int UPGRADE_DAMAGE_AMT = 3;
    private static final int FORM_DAMAGE_AMT = 3;

    public PegasusDescent() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = FORM_DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            this.addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
        }
        int hits = getHitsNb(p, energyOnUse, this);
        for (int i = 0; i < hits; ++i) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    m, new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
//        AbstractCard proxy = this;
//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                int hits = getHitsNb(p, energyOnUse, proxy);
//                for (int i = 0; i < hits; ++i) {
//                    AbstractDungeon.actionManager.addToBottom(new DamageAction(
//                            m, new DamageInfo(p, damage, damageTypeForTurn),
//                            AbstractGameAction.AttackEffect.BLUNT_LIGHT));
//                }
//                this.isDone = true;
//            }
//        });
//        formEffect(HarpyGene.ID, () -> addToBot(new CentaurGene().getChannelAction()));
        formEffect(CentaurGene.ID);
    }

    @Override
    public void applyPowers() {
        alterDamageAround(super::applyPowers);
        int hits = getHitsNb(AbstractDungeon.player, EnergyPanel.getCurrentEnergy(), this);
        rawDescription = EXTENDED_DESCRIPTION[0] + hits + EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        alterDamageAround(() -> super.calculateCardDamage(mo));
    }

    private void alterDamageAround(Runnable supercall) {
        this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT:0) + (isPlayerInThisForm(CentaurGene.ID) ? this.magicNumber : 0);
        supercall.run();
        this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT:0);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new PegasusDescent();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(CentaurGene.ID)) {
            this.glowColor = CentaurGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    private static int getHitsNb(AbstractPlayer player, int currentEnergy, AbstractCard pegasusDescent) {
        return 1 + (player.hand.group.stream()
                .mapToInt(card -> card.cost == -1 ? currentEnergy : card.costForTurn)
                .filter(cost -> cost > 0)
                .sum() - (pegasusDescent.cost == -1 ? currentEnergy : pegasusDescent.costForTurn)) / 3;
    }
//    public static class PegasusHits extends DynamicVariable {
//
//        @Override
//        public int baseValue(AbstractCard card) {
//            return 1;
//        }
//
//        @Override
//        public int value(AbstractCard card) {
//            int hits = 1 + (AbstractDungeon.player.hand.group.stream()
//                    .mapToInt(c -> c.cost == -1 ? EnergyPanel.getCurrentEnergy() : c.costForTurn)
//                    .filter(cost -> cost > 0)
//                    .sum() - (card.cost == -1 ? EnergyPanel.getCurrentEnergy() : card.costForTurn)) / 3;
//            return hits;
//        }
//
//        @Override
//        public boolean isModified(AbstractCard card) {
//            return false;
//        }
//
//        @Override
//        public String key() {
//            return "evolutionmod:PegasusHits";
//        }
//
//        @Override
//        public boolean upgraded(AbstractCard card) {
//            return false;
//        }
//    }
}