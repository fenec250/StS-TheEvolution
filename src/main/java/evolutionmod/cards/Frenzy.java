package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
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
    private static final int COST = 0;
    private static final int DAMAGE_AMT = 3;
    private static final int FRENZY_AMT = 3;
    private static final int RAGE_AMT = 2;
    private static final int UPGRADE_FRENZY_AMT = 2;
    private static final int UPGRADE_RAGE_AMT = 1;

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
        int rageAmount = RAGE_AMT + (this.upgraded ? UPGRADE_RAGE_AMT : 0);
        formEffect(BeastGene.ID, () -> addToBot(new ApplyPowerAction(p, p,
                    new RagePower(p, rageAmount))));
    }

    @Override
    public void applyPowers() {
        this.baseDamage = DAMAGE_AMT;
        int energy = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(c -> c.type == CardType.ATTACK
                        && c != this)
                .mapToInt(c -> c.cost >= 0 ? c.cost : (c.cost == -1 ? c.energyOnUse : 0))
                .sum();
        this.baseDamage += energy * this.magicNumber;

        super.applyPowers();

        this.baseDamage = DAMAGE_AMT;
        this.isDamageModified = this.baseDamage != this.damage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = DAMAGE_AMT;
        int energy = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(c -> c.type == CardType.ATTACK
                        && c != this)
                .mapToInt(c -> c.cost >= 0 ? c.cost : c.energyOnUse)
                .sum();
        this.baseDamage += energy * this.magicNumber;

        super.calculateCardDamage(mo);

        this.baseDamage = DAMAGE_AMT;
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
            this.upgradeMagicNumber(UPGRADE_FRENZY_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(BeastGene.ID)) {
            this.glowColor = BeastGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}