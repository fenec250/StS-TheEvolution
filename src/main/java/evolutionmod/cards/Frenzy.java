package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RagePower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.patches.AbstractCardEnum;

public class Frenzy
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Frenzy";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BeastAtt.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 8;
    private static final int FRENZY_AMT = 3;
    private static final int RAGE_AMT = 2;
    private static final int UPGRADE_FRENZY_AMT = 2;

    public Frenzy() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = FRENZY_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (AbstractGene.isPlayerInThisForm(BeastGene.ID)) {
            addToBot(new ApplyPowerAction(p, p, new RagePower(p, RAGE_AMT)));
        } else {
            addToBot(new ChannelAction(new BeastGene()));
        }
    }

    @Override
    public void applyPowers() {
        this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_FRENZY_AMT : 0);
        int energy = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(c -> c.type == CardType.ATTACK
                        && c != this)
                .mapToInt(c -> c.cost >= 0 ? c.cost : c.energyOnUse)
                .sum();
        this.baseDamage += energy * this.magicNumber;

        super.applyPowers();

        this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_FRENZY_AMT : 0);
        this.isDamageModified = this.baseDamage != this.damage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_FRENZY_AMT : 0);
        int energy = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(c -> c.type == CardType.ATTACK
                        && c != this)
                .mapToInt(c -> c.cost >= 0 ? c.cost : c.energyOnUse)
                .sum();
        this.baseDamage += energy * this.magicNumber;

        super.calculateCardDamage(mo);

        this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_FRENZY_AMT : 0);
        this.isDamageModified = this.baseDamage != this.damage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Frenzy();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_FRENZY_AMT);
        }
    }
}