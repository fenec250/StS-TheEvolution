package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.EvolutionEnum;

public class DeathKiss
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:DeathKiss";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/DeathKiss.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 2;
    private static final int UPGRADE_DAMAGE_AMT = 1;
    private static final int POISON_AMT = 3;
    private static final int UPGRADE_POISON_AMT = 1;


    public DeathKiss() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = POISON_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        int poison = this.magicNumber;
        this.exhaust = false;
        boolean inForm = isPlayerInThisForm(ShadowGene2.ID);
        if (inForm) {
            poison += 2*this.magicNumber;
            this.exhaust = true;
        }
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, poison)));
        formEffect(ShadowGene2.ID);
    }
    @Override
    public void applyPowers() {
        alterMagicNumberAround(super::applyPowers);
    }

    private void alterMagicNumberAround(Runnable supercall) {
        this.baseMagicNumber = POISON_AMT + (upgraded ? UPGRADE_POISON_AMT : 0);
        this.baseMagicNumber = baseMagicNumber * (isPlayerInThisForm(ShadowGene2.ID) ? 3 : 1);
        supercall.run();
        this.baseMagicNumber = POISON_AMT + (upgraded ? UPGRADE_POISON_AMT : 0);
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_POISON_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(ShadowGene2.ID)) {
            this.glowColor = ShadowGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}